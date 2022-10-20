cmake_minimum_required(VERSION 3.19)
project(Qt_demo)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_PREFIX_PATH "/usr/local/Cellar/qt@5/5.15.6")
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

set(QT_VERSION 5)
set(REQUIRED_LIBS Core Gui Widgets)
set(REQUIRED_LIBS_QUALIFIED Qt5::Core Qt5::Gui Qt5::Widgets)

include_directories("/usr/local/opt/ffmpeg@2.8/include")
link_directories("/usr/local/opt/ffmpeg@2.8/lib")

find_package(OpenCV REQUIRED)
include_directories(sample5 ${OpenCV_INCLUDE_DIRS})



add_executable(${PROJECT_NAME} main.cpp mainwindow.cpp mainwindow.h mainwindow.ui ffmpegdecoder.cpp ffmpegdecoder.h)

if (NOT CMAKE_PREFIX_PATH)
    message(WARNING "CMAKE_PREFIX_PATH is not defined, you may need to set it "
            "(-DCMAKE_PREFIX_PATH=\"path/to/Qt/lib/cmake\" or -DCMAKE_PREFIX_PATH=/usr/include/{host}/qt{version}/ on Ubuntu)")
endif ()

find_package(Qt${QT_VERSION} COMPONENTS ${REQUIRED_LIBS} REQUIRED)
target_link_libraries(${PROJECT_NAME} ${REQUIRED_LIBS_QUALIFIED})
#target_link_libraries(${PROJECT_NAME} ${AVCODEC_LIBRARY})
#target_link_libraries(${PROJECT_NAME} ${AVFORMAT_LIBRARY})
#target_link_libraries(${PROJECT_NAME} ${AVUTIL_LIBRARY})
#target_link_libraries(${PROJECT_NAME} ${AVDEVICE_LIBRARY})
target_link_libraries(${PROJECT_NAME} ${OpenCV_LIBS} avcodec avformat avutil avdevice swscale)
