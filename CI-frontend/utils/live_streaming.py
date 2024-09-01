import streamlit as st
import requests
import os

# API 기본 URL 설정 (백엔드에서 변경해 주세요)
BACKEND_API = os.environ.get("BACKEND_API")


# 하드코딩된 라이브 미디어 목록 데이터
def fetch_live_media_list():
    """하드코딩된 라이브 미디어 목록을 반환합니다."""
    return [
        {
            "id": 3,
            "liveStreamingUrl": "aws-ivs.m3u8",
            "title": "Live Event 3",
            "startedAt": "2024.08.23 08:10",
            "endedAt": None
        },
        {
            "id": 2,
            "liveStreamingUrl": "aws-ivs.m3u8",
            "title": "Live Event 2",
            "startedAt": "2024.08.23 08:10",
            "endedAt": None
        },
        {
            "id": 1,
            "liveStreamingUrl": "aws-ivs.m3u8",
            "title": "Live Event 1",
            "startedAt": "2024.08.23 08:10",
            "endedAt": None
        }
    ]


def show():
    live_media_list = fetch_live_media_list()

    if live_media_list:
        html_code = '<div style="display: flex; overflow-x: scroll; gap: 20px; padding: 10px;">'
        for i, live in enumerate(live_media_list):
            if 'title' in live and 'liveStreamingUrl' in live:
                html_code += f"""
                    <div style="min-width: 300px; width: 300px; height: 600px; flex-shrink: 0; display: flex; flex-direction: column; justify-content: space-between;">
                        <div style="flex-grow: 0; margin-bottom: 10px;">
                            <p style="margin: 0; font-weight: bold;">{live['title']}</p>
                        </div>
                        <div style="flex-grow: 1;">
                            <video id="video_{i}" controls style="width: 100%; height: 100%;">
                                <source type="application/x-mpegURL" src="{live['liveStreamingUrl']}">
                            </video>
                            <script src="https://cdn.jsdelivr.net/npm/hls.js@latest"></script>
                            <script>
                                if(Hls.isSupported()) {{
                                    var video = document.getElementById('video_{i}');
                                    var hls = new Hls();
                                    hls.loadSource('{live['liveStreamingUrl']}');
                                    hls.attachMedia(video);
                                    hls.on(Hls.Events.MANIFEST_PARSED,function() {{
                                        video.play();
                                    }});
                                }}
                                else if (video.canPlayType('application/vnd.apple.mpegurl')) {{
                                    video.src = '{live['liveStreamingUrl']}';
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
        st.write("라이브 스트리밍 목록을 가져올 수 없습니다.")
