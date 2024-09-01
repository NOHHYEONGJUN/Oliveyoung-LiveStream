import streamlit as st
import requests
import os
import pandas as pd

# API 기본 URL 설정
BACKEND_API = os.environ.get("BACKEND_API")


def fetch_latest_vod_list():
    """최신 VOD 목록을 캐싱하여 가져옵니다."""
    try:
        fallback_url = f"{BACKEND_API}/media/video/latest"
        fallback_response = requests.get(fallback_url)
        fallback_response.raise_for_status()
        return fallback_response.json()
    except requests.exceptions.RequestException as e:
        st.error(f"VOD 목록을 가져오는데 실패했습니다: {e}")
        return []


def fetch_vod_list():
    """API로부터 VOD 목록을 가져옵니다."""
    jwt_token = st.session_state.get("access_token")

    # API 엔드포인트 설정: 토큰이 있는 경우 개인화된 목록, 없는 경우 최신 목록
    if jwt_token:
        api_url = f"{BACKEND_API}/media/video/personalized"
    else:
        return fetch_latest_vod_list()  # 기본적으로 캐싱된 최신 목록을 반환

    try:
        headers = {"Authorization": f"Bearer {jwt_token}"}

        response = requests.get(api_url, headers=headers)

        # 404 또는 500 응답인 경우 캐시된 최신 목록을 가져옴
        if response.status_code in {404, 500}:
            st.warning(f"개인화된 VOD 목록을 가져오는데 실패했습니다: {response.status_code}")
            return fetch_latest_vod_list()

        response.raise_for_status()  # 그 외의 HTTP 에러 발생 시 예외 발생
        return response.json()

    except requests.exceptions.RequestException as e:
        st.error(f"VOD 목록을 가져오는데 실패했습니다: {e}")
        return fetch_latest_vod_list()  # 예외 발생 시에도 캐시된 최신 목록을 반환


def show():
    # VOD 목록 가져오기
    global selected_vod_url
    vod_list = fetch_vod_list()

    if vod_list:
        html_code = '<div style="display: flex; overflow-x: scroll; gap: 20px; padding: 10px;">'
        for i, vod in enumerate(vod_list):
            if 'videoId' in vod and 'vodUrl' in vod:
                html_code += f"""
                    <div style="min-width: 300px; width: 300px; height: 600px; flex-shrink: 0; display: flex; flex-direction: column; justify-content: space-between;">
                        <div style="flex-grow: 0; margin-bottom: 10px;">
                            <p style="margin: 0; font-weight: bold;">{vod['videoId']}</p>
                        </div>
                        <div style="flex-grow: 1;">
                            <video id="video_{i}" controls style="width: 100%; height: 100%;">
                                <source type="application/x-mpegURL" src="{vod['vodUrl']}">
                            </video>
                            <script src="https://cdn.jsdelivr.net/npm/hls.js@latest"></script>
                            <script>
                                if(Hls.isSupported()) {{
                                    var video = document.getElementById('video_{i}');
                                    var hls = new Hls();
                                    hls.loadSource('{vod['vodUrl']}');
                                    hls.attachMedia(video);
                                    hls.on(Hls.Events.MANIFEST_PARSED,function() {{
                                        video.play();
                                    }});
                                }}
                                else if (video.canPlayType('application/vnd.apple.mpegurl')) {{
                                    video.src = '{vod['vodUrl']}';
                                    video.addEventListener('canplay',function() {{
                                        video.play();
                                    }});
                                }}
                            </script>
                        </div>
                    </div>
                """
        html_code += '</div>'
        st.components.v1.html(html_code, height=700)  # HTML과 JS 코드 전체를 st.components.v1.html로 이동
    else:
        st.warning("다시보기 목록을 가져올 수 없습니다.")
