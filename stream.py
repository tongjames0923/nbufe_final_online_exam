#base on ffmpeg and opencv
# need to install environment for service 
#  git clone https://github.com/iizukanao/node-rtsp-rtmp-server.git
#  cd node-rtsp-rtmp-server
#  npm install -d
#  npm install --global coffeescript
#then run rtsp-server
#  cd node-rtsp-rtmp-server         
#  sudo coffee server.coffee

#after all,run this cmd to push streams to server
#ffmpeg  -f avfoundation -framerate 30 -s 640x480 -i "0" -vcodec libx264 -preset:v ultrafast -tune:v zerolatency -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:80/test