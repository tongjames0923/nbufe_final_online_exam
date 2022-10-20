import cv2

import subprocess
#base on ffmpeg and opencv
# need to install environment for service 
#  git clone https://github.com/iizukanao/node-rtsp-rtmp-server.git
#  cd node-rtsp-rtmp-server
#  npm install -d
#  npm install --global coffeescript
#then run rtsp-server
#  cd node-rtsp-rtmp-server         
#  sudo coffee server.coffee

#after all,run this file to push streams to server



 
class Put_rtsp():
    def __init__(self,cap):
        self.cap = cv2.VideoCapture(cap)
        # self.cap = cv2.VideoCapture("rtsp://192.168.111.131:6666/live")
        # self.cap = cv2.VideoCapture(0)
        self.width = int(self.cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        self.height = int(self.cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        self.number_frames = 0
        self.fps = int(self.cap.get(cv2.CAP_PROP_FPS)) % 100
 
 
    def on_need_data(self):
        if self.cap.isOpened():
            ret, frame = self.cap.read()
            #TODO 图像处理任务           
            return frame
 
 
 
rtsp_o = 0
rtsp_p = 'rtsp://localhost:80/live/STREAMNAME'
# 读取视频并获取属性
cap = cv2.VideoCapture(rtsp_o)
width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
# print(width,height)
# fps = int(cap.get(cv2.CAP_PROP_FPS)) % 100
fps=12
size = (width,height)
sizeStr = str(size[0]) + 'x' + str(size[1])
 
 
command = ['ffmpeg',
           '-y', '-an',
           '-re',
           '-f', 'rawvideo',
           '-pix_fmt', 'bgr24',
           '-s', sizeStr,
           '-r', str(fps),
           '-i', '-',
           '-c:v','libx264',
           '-g', '1',
           '-maxrate:v', '6M',
           '-minrate:v', '2M',
           '-bufsize:v', '4M',
           '-pix_fmt','yuv420p',
           # '-profile:v','high444',
           '-preset','fast',#'ultrafast',# 'superfast',
           '-tune', 'zerolatency',
           # '-b:v', '4M',
           '-f', 'rtsp',
            rtsp_p]
 
 
pipe = subprocess.Popen(command
                        , shell=False
                        , stdin=subprocess.PIPE
                        )
 
 
 
while cap.isOpened():
    f = Put_rtsp(rtsp_o)
    frame  = f.on_need_data()
    data = frame.tostring()
    pipe.stdin.write(data)
 
cap.release()
pipe.terminate()