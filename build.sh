#!/bin/sh

android update lib-project -p . -t android-8
# http://code.google.com/p/android/issues/detail?id=13024#c21
sed -i 's#<setup />#<setup />\n\t<path id="android.libraries.src"><path refid="project.libraries.src" /></path>\n\t<path id="android.libraries.jars"><path refid="project.libraries.jars" /></path>#g' build.xml
ant compile
