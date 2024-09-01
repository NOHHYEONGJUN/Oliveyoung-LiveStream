import streamlit as st
import requests
import os
import pandas as pd

# 백엔드 API URL 설정
BACKEND_API = os.environ.get("BACKEND_API")

def fetch_order_list(jwt_token):
    """API로부터 주문 목록을 가져옵니다."""
    try:
        headers = {"Authorization": f"Bearer {jwt_token}"}
        response = requests.get(f"{BACKEND_API}/orders", headers=headers)

        if response.status_code == 404:
            st.warning("주문 목록이 없습니다")
            return []

        response.raise_for_status()  # HTTP 에러 발생 시 예외 발생
        return response.json()

    except requests.exceptions.RequestException as e:
        st.error(f"주문 목록을 가져오는데 실패했습니다: {e}")
        return []



def show():
    # JWT 토큰 가져오기
    jwt_token = st.session_state.get("access_token")

    if jwt_token:
        # JWT가 존재하는 경우 주문 목록 가져오기
        order_list = fetch_order_list(jwt_token)

        if order_list:
            # JSON 데이터를 DataFrame으로 변환
            df = pd.DataFrame(order_list)

            # 데이터프레임을 테이블로 표시
            st.write("### 주문 목록")
            st.dataframe(df)
        else:
            st.warning("주문 목록을 불러오는 데 실패했습니다.")
    else:
        # JWT가 없는 경우 경고 메시지 표시
        st.warning("로그인을 해야 주문 목록을 볼 수 있어요.")
