# Geodroid Native Libaries

This document describes how to build the native libaries for Geodroid.

## Pre-requisites

1. Install the [Android NDK](https://developer.android.com/tools/sdk/ndk/index.html).

1. Set the environment variable `NDK_ROOT` to point to the NDK installation. For example:

        export NDK_ROOT=/usr/local/Cellar/android-ndk/r9d

1. Create a toolchain for the target android platform. For example:

        $NDK_ROOT/build/tools/make-standalone-toolchain.sh --platform=android-18  \
            --install-dir=/opt/android/android-18/toolchain

1. Add the toolchain `bin` directory to the `PATH`:

        export PATH=/opt/android/android-18/toolchain/bin:$PATH

## Proj

1. Switch to the `jni` directory.

1. Edit the ``Android.mk`` file and uncomment the `proj.mk` inclusion.

1. Download and unpack the [proj 4.8.0](http://download.osgeo.org/proj/proj-4.8.0.tar.gz) sources.
   The resulting directory structure should look like the following:

        jni/
          Android.mk
          Application.mk
          proj.mk
          proj-4.8.0/

1. Change to proj source directory and apply the following patches:

        cd proj-4.8.0
        patch -p1 < ../pj_open_lib.c.patch
        patch -p2 < ../pj_init.c.patch

 1. Configure:

        ./configure --host=arm-linux-eabi  --with-jni

1. Generate JNI headers:

        cd jniwrap
        ant do_javah

1. Change back to `jni` directory and build:

        cd ../../
        ndk-build

The resulting library should reside in `../libs/armeabi/libproj.so`. 

### References

* https://bitbucket.org/nutiteq/android-map-samples
* http://trac.osgeo.org/proj/ticket/125
* http://trac.osgeo.org/proj/ticket/204

## GDAL/OGR

1. Switch to the `jni` directory.

1. Edit the `Android.mk` file and uncomment the `gdal.mk` inclusion.

1. Download and unpack the [gdal 1.9.2](http://download.osgeo.org/gdal/gdal-1.9.2.tar.gz) sources.
   The resulting directory structure should look like the following:

        jni/
          Android.mk
          Application.mk
          gdal.mk
          gdal-1.9.2

1. Change to the gdal directory and configure:

        cd gdal-1.9.2
        rm config.sub config.guess
        wget http://git.savannah.gnu.org/cgit/config.git/plain/config.sub
        wget http://git.savannah.gnu.org/cgit/config.git/plain/config.guess
        CFLAGS="-mthumb" CXXFLAGS="-mthumb" LIBS="-lsupc++ -lstdc++"  ./configure --host=arm-linux-androideabi --prefix=`pwd`/../../ --without-gif --with-threads --with-ogr  --with-geos --with-libz=internal

1. Apply the following patches:

        patch -p1 < ../cpl_config.h.patch
        patch -p1 < ../typemaps_java.i.patch

1. Make:

        make
        make install

1. Change to the `swig` directory and make:

        cd swig
        make

1. Change 

### References

* https://github.com/nutiteq/gdal/wiki/AndroidHowto
* http://trac.osgeo.org/gdal/wiki/BuildingForAndroid








