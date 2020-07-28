#!/bin/bash

javac -cp /usr/lib/hadoop/*:/usr/lib/hadoop/client-0.20/* -d ~/Documents/shared/MineProcess ~/Documents/shared/MineProcess/Util.java 
~/Documents/shared/MineProcess/Extract.java ~/Documents/shared/MineProcess/LightPoint.java ~/Documents/shared/MineProcess/Point.java ~/Documents/shared/MineProcess/Centroid.java ~/Documents/shared/MineProcess/Box.java ~/Documents/shared/MineProcess/Cluster.java ~/Documents/shared/MineProcess/Master.java ~/Documents/shared/MineProcess/Polish.java ~/Documents/shared/MineProcess/Scan.java ~/Documents/shared/MineProcess/Sift.java ~/Documents/shared/MineProcess/Calc.java ~/Documents/shared/MineProcess/ColorEdit.java ~/Documents/shared/MineProcess/Constants.java ~/Documents/shared/MineProcess/Converter.java ~/Documents/shared/MineProcess/Format.java ~/Documents/shared/MineProcess/Radius.java ~/Documents/shared/MineProcess/Slide.java ~/Documents/shared/MineProcess/PictureFrame.java ~/Documents/shared/MineProcess/Picture.java ~/Documents/shared/MineProcess/SimplePicture.java ~/Documents/shared/MineProcess/Convert.java ~/Documents/shared/MineProcess/DigitalPicture.java ~/Documents/shared/MineProcess/ImageArrEdit.java ~/Documents/shared/MineProcess/Pixel.java ~/Documents/shared/MineProcess/MacroMaster.java ~/Documents/shared/MineProcess/Runner.java 
echo "COMPILED!"
jar -cvf Runner.jar -C ~/Documents/shared/MineProcess .
echo "STARTING HADOOP..."
hadoop jar Runner.jar Runner /user/cloudera/MineProcess/input/ /user/cloudera/MineProcess/output/mine$1
echo "FINISHED!"
