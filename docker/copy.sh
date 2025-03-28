#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
#cp ../sql/ry_20240629.sql ./mysql/db
cp ../sql/financial_config.sql ./mysql/db

# copy html
#echo "begin copy html "
#cp -r ../financial-backend-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy financial-backend-gateway "
cp ../financial-backend-gateway/target/financial-backend-gateway.jar ./financial-backend/gateway/jar

echo "begin copy financial-backend-auth "
cp ../financial-backend-auth/target/financial-backend-auth.jar ./financial-backend/auth/jar

echo "begin copy financial-backend-visual "
cp ../financial-backend-visual/financial-backend-monitor/target/financial-backend-visual-monitor.jar  ./financial-backend/visual/monitor/jar

echo "begin copy financial-backend-modules-system "
cp ../financial-backend-modules/financial-backend-system/target/financial-backend-modules-system.jar ./financial-backend/modules/system/jar

echo "begin copy financial-backend-modules-file "
cp ../financial-backend-modules/financial-backend-file/target/financial-backend-modules-file.jar ./financial-backend/modules/file/jar

echo "begin copy financial-backend-modules-job "
cp ../financial-backend-modules/financial-backend-job/target/financial-backend-modules-job.jar ./financial-backend/modules/job/jar

echo "begin copy financial-backend-modules-gen "
cp ../financial-backend-modules/financial-backend-gen/target/financial-backend-modules-gen.jar ./financial-backend/modules/gen/jar

