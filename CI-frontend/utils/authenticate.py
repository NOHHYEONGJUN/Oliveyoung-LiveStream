import os
import warnings

import streamlit as st
from dotenv import load_dotenv
import requests
import base64
import json

# ------------------------------------
# 경고 메시지 숨기기 설정
# ------------------------------------
warnings.filterwarnings("ignore", category=DeprecationWarning)

# ------------------------------------
# Read constants from environment file
# ------------------------------------
load_dotenv()
COGNITO_DOMAIN = os.environ.get("COGNITO_DOMAIN")
CLIENT_ID = os.environ.get("CLIENT_ID")
CLIENT_SECRET = os.environ.get("CLIENT_SECRET")
APP_URI = os.environ.get("APP_URI")
BACKEND_API = os.environ.get("BACKEND_API")  # 백엔드 API URL 추가


# ------------------------------------
# Initialise Streamlit state variables
# ------------------------------------
def initialise_st_state_vars():
    """
    Initialise Streamlit state variables.
    Returns:
        Nothing.
    """
    if "auth_code" not in st.session_state:
        st.session_state["auth_code"] = ""
    if "authenticated" not in st.session_state:
        st.session_state["authenticated"] = False


# ----------------------------------
# Get authorization code after login
# ----------------------------------
def get_auth_code():
    """
    Gets auth_code state variable.
    Returns:
        Nothing.
    """
    auth_query_params = st.experimental_get_query_params()
    try:
        auth_code = dict(auth_query_params)["code"][0]
    except (KeyError, TypeError):
        auth_code = ""
    return auth_code


# ----------------------------------
# Set authorization code after login
# ----------------------------------
def set_auth_code():
    """
    Sets auth_code state variable.
    Returns:
        Nothing.
    """
    initialise_st_state_vars()
    auth_code = get_auth_code()
    st.session_state["auth_code"] = auth_code


# -------------------------------------------------------
# Use authorization code to get user access and id tokens
# -------------------------------------------------------
def get_user_tokens(auth_code):
    """
    Gets user tokens by making a post request call.
    Args:
        auth_code: Authorization code from cognito server.
    Returns:
        {
        'access_token': access token from cognito server if user is successfully authenticated.
        'id_token': access token from cognito server if user is successfully authenticated.
        }
    """
    # Variables to make a post request
    token_url = f"{COGNITO_DOMAIN}/oauth2/token"
    client_secret_string = f"{CLIENT_ID}:{CLIENT_SECRET}"
    client_secret_encoded = str(
        base64.b64encode(client_secret_string.encode("utf-8")), "utf-8"
    )
    headers = {
        "Content-Type": "application/x-www-form-urlencoded",
        "Authorization": f"Basic {client_secret_encoded}",
    }
    body = {
        "grant_type": "authorization_code",
        "client_id": CLIENT_ID,
        "code": auth_code,
        "redirect_uri": APP_URI,
        "scope": "openid email profile"  # 필요한 스코프 추가
    }
    token_response = requests.post(token_url, headers=headers, data=body)
    try:
        access_token = token_response.json()["access_token"]
        id_token = token_response.json()["id_token"]
    except (KeyError, TypeError):
        access_token = ""
        id_token = ""
    return access_token, id_token


# ---------------------------------------------
# Use access token to retrieve user information
# ---------------------------------------------
def get_user_info(id_token):
    """
    Gets user info from aws cognito server.
    Args:
        id_token: string ID token from the aws cognito user pool
        retrieved using the access code.
    Returns:
        dict: User information (cognitoUserId, email, username, gender, age).
    """
    userinfo_url = f"{COGNITO_DOMAIN}/oauth2/userInfo"
    headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "Authorization": f"Bearer {id_token}",
    }
    userinfo_response = requests.get(userinfo_url, headers=headers)
    user_info = userinfo_response.json()

    # 필요한 사용자 정보 추출
    cognito_user_id = user_info.get("sub")  # 일반적으로 sub가 Cognito User ID입니다.
    email = user_info.get("email")
    username = user_info.get("preferred_username") or user_info.get("username")
    gender = user_info.get("gender")
    age = int(user_info.get("custom:age", 0))  # 사용자 지정 속성은 "custom:" 접두사로 가져옴

    return {
        "cognitoUserId": cognito_user_id,
        "email": email,
        "username": username,
        "gender": gender,
        "age": age
    }


# ---------------------------------------------
# Send user info and tokens to the backend for login/signup
# ---------------------------------------------
def send_user_info_to_backend(url, user_info, id_token):
    """
    Sends user information and ID token to the backend for processing (login or signup).
    Args:
        url: The backend API endpoint.
        user_info: Dictionary containing the user information.
        id_token: The ID token from Cognito.
    Returns:
        Response from the backend.
    """
    headers = {
        "Authorization": f"Bearer {id_token}",
        "Content-Type": "application/json"
    }
    response = requests.post(url, json=user_info, headers=headers)
    return response


# -----------------------------
# Set Streamlit state variables
# -----------------------------
def set_st_state_vars():
    """
    Sets the Streamlit state variables after user authentication.
    Sends user info to backend for login.
    Returns:
        Nothing.
    """
    initialise_st_state_vars()
    auth_code = get_auth_code()
    access_token, id_token = get_user_tokens(auth_code)
    if access_token != "":
        st.session_state["auth_code"] = auth_code
        st.session_state["authenticated"] = True
        st.session_state["access_token"] = access_token
        st.session_state["id_token"] = id_token


# -----------------------------
# Handle signup process
# -----------------------------
def signup():
    """
    Handles the signup process.
    Sends user info to backend for signup.
    Returns:
        Nothing.
    """
    initialise_st_state_vars()
    auth_code = get_auth_code()
    access_token, id_token = get_user_tokens(auth_code)
    if access_token != "":
        st.session_state["auth_code"] = auth_code
        st.session_state["authenticated"] = True
        st.session_state["access_token"] = access_token
        st.session_state["id_token"] = id_token

        # 사용자 정보 가져오기
        user_info = get_user_info(id_token)

        # 회원가입 시 사용자 정보를 백엔드로 전송
        signup_response = send_user_info_to_backend(f"{BACKEND_API}/auth/signup", user_info, id_token)
        if signup_response.status_code != 200:
            st.error("회원가입 처리 중 오류가 발생했습니다.")


# -----------------------------
# Login/ Logout HTML components
# -----------------------------
login_link = f"{COGNITO_DOMAIN}/login?client_id={CLIENT_ID}&response_type=code&scope=email+openid+profile+aws.cognito.signin.user.admin&redirect_uri={APP_URI}"
logout_link = f"{COGNITO_DOMAIN}/logout?client_id={CLIENT_ID}&logout_uri={APP_URI}"

html_css_login = """
<style>
.button-login {
    background-color: white;
    color: black !important;
    padding: 0.5em 1.0em;
    text-decoration: none;
    text-transform: uppercase;
    font-size: 15px;
}
.button-login:hover {
    background-color: #f0f0f0;
    text-decoration: none;
}
.button-login:active {
    background-color: #e0e0e0;
}
</style>
"""

html_button_login = (
        html_css_login
        + f"<a href='{login_link}' class='button-login' target='_self'>Log In</a>"
)
html_button_logout = (
        html_css_login
        + f"<a href='{logout_link}' class='button-login' target='_self'>Log Out</a>"
)


def button_login():
    """
    Returns:
        Html of the login button.
    """
    return st.sidebar.markdown(f"{html_button_login}", unsafe_allow_html=True)


def button_logout():
    """
    Returns:
        Html of the logout button.
    """
    return st.sidebar.markdown(f"{html_button_logout}", unsafe_allow_html=True)
