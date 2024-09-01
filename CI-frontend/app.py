import streamlit as st
from PIL import Image
from streamlit_option_menu import option_menu
from utils import authenticate, video, live_streaming, order
from utils.helpers import load_css

# 페이지 설정
st.set_page_config(
    layout="wide"
)

st.title("PROD-SEOUL")

# 로고 이미지 경로
logo_path = "assets/o-logo2.png"

# CSS 적용
load_css("assets/style.css")

# Check authentication when user lands on the home page.
authenticate.set_st_state_vars()

# 사이드바에 로고 이미지 표시
logo = Image.open(logo_path)
st.sidebar.image(logo, use_column_width=True)


with st.sidebar:
    if st.session_state["authenticated"]:
        authenticate.button_logout()
    else:
        authenticate.button_login()

    # 메뉴 구성
    menu_options = ["라이브 스트리밍", "다시보기", "주문 목록"]

    choose = option_menu("MENU", menu_options,
                         styles={
                             "container": {"padding": "5!important", "background-color": "#fafafa"},
                             "nav-link": {"font-size": "16px", "text-align": "left", "margin": "0px",
                                          "--hover-color": "#eee"},
                             "nav-link-selected": {"background-color": "#02ab21"},
                         }
                         )

# page routing
if choose == "라이브 스트리밍":
    live_streaming.show()

if choose == "다시보기":
    video.show()

if choose == "주문 목록":
    order.show()

# space at the bottom of the logo image
st.markdown("<br><br><br>", unsafe_allow_html=True)
