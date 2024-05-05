#!/usr/bin/env bash

#######################################################
#
#	modified build_wine.sh from Kron4ek
#	
#	https://github.com/Kron4ek/Wine-Builds.git
#
#######################################################

######### vvv change dir vvvv #########################


export BUILD_DIR="${HOME}"/Desktop/xinput_dev/work_temp


#######################################################



if [ $EUID = 0 ] && [ -z "$ALLOW_ROOT" ]; then
	echo "Do not run this script as root!"
	echo
	echo "If you really need to run it as root and you know what you are doing,"
	echo "set the ALLOW_ROOT environment variable."

	exit 1
fi


export BOOTSTRAP_X64=/opt/chroots/bionic64_chroot
export BOOTSTRAP_X32=/opt/chroots/bionic32_chroot
export scriptdir="$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")"
export CC="gcc-9"
export CXX="g++-9"
export CROSSCC_X32="i686-w64-mingw32-gcc"
export CROSSCXX_X32="i686-w64-mingw32-g++"
export CROSSCC_X64="x86_64-w64-mingw32-gcc"
export CROSSCXX_X64="x86_64-w64-mingw32-g++"
export CFLAGS_X32="-march=i686 -msse2 -mfpmath=sse -O2 -ftree-vectorize"
export CFLAGS_X64="-march=x86-64 -msse3 -mfpmath=sse -O2 -ftree-vectorize"
export LDFLAGS="-Wl,-O1,--sort-common,--as-needed"
export CROSSCFLAGS_X32="${CFLAGS_X32}"
export CROSSCFLAGS_X64="${CFLAGS_X64}"
export CROSSLDFLAGS="${LDFLAGS}"



build_with_bwrap () {
	if [ "${1}" = "32" ]; then
		BOOTSTRAP_PATH="${BOOTSTRAP_X32}"
	else
		BOOTSTRAP_PATH="${BOOTSTRAP_X64}"
	fi

	if [ "${1}" = "32" ] || [ "${1}" = "64" ]; then
		shift
	fi

    bwrap --ro-bind "${BOOTSTRAP_PATH}" / --dev /dev --ro-bind /sys /sys \
		  --proc /proc --tmpfs /tmp --tmpfs /home --tmpfs /run --tmpfs /var \
		  --tmpfs /mnt --tmpfs /media --bind "${BUILD_DIR}" "${BUILD_DIR}" \
		  --bind-try "${XDG_CACHE_HOME}"/ccache "${XDG_CACHE_HOME}"/ccache \
		  --bind-try "${HOME}"/.ccache "${HOME}"/.ccache \
		  --setenv PATH "/bin:/sbin:/usr/bin:/usr/sbin" \
			"$@"
}

cd "${BUILD_DIR}" || exit 1
cp -f ../input/main.c wine/dlls/xinput1_3/main.c

BWRAP64="build_with_bwrap 64"
BWRAP32="build_with_bwrap 32"





build_xinput_dll() {

    export CROSSCC="${CROSSCC_X64}"
    export CROSSCXX="${CROSSCXX_X64}"
    export CFLAGS="${CFLAGS_X64}"
    export CXXFLAGS="${CFLAGS_X64}"
    export CROSSCFLAGS="${CROSSCFLAGS_X64}"
    export CROSSCXXFLAGS="${CROSSCFLAGS_X64}"

    echo "try Building Xinput $1.dll - 64bit"
    cd "${BUILD_DIR}/build64/dlls/$1/x86_64-windows"
    if [ -e "$1.dll" ]; then
        rm main.o "$1.dll"
    fi
    cd ..
    ${BWRAP64} make -j$(nproc)
    cd x86_64-windows
    if [ -e "$1.dll" ]; then
        echo
        echo -e "\033[32m success, YEAH! \033[0m"
        cp -f "$1.dll" ../../../../../output/64/"$1.dll"
    else
        echo
        echo -e "\033[31m fail :( \033[0m"
        echo
        exit
    fi

    cd ../../../../../

    export CROSSCC="${CROSSCC_X32}"
    export CROSSCXX="${CROSSCXX_X32}"
    export CFLAGS="${CFLAGS_X32}"
    export CXXFLAGS="${CFLAGS_X32}"
    export CROSSCFLAGS="${CROSSCFLAGS_X32}"
    export CROSSCXXFLAGS="${CROSSCFLAGS_X32}"

    echo "try Building Xinput $1.dll - 32bit"
    cd "${BUILD_DIR}/build32-tools/dlls/$1/i386-windows"
    if [ -e "$1.dll" ]; then
        rm main.o "$1.dll"
    fi
    cd ..
    ${BWRAP32} make -j$(nproc)
    cd i386-windows
    if [ -e "$1.dll" ]; then
        echo
        echo -e "\033[32m success, YEAH! \033[0m"
        cp -f "$1.dll" ../../../../../output/32/"$1.dll"
    else
        echo
        echo -e "\033[31m fail :( \033[0m"
        echo
        exit
    fi

}


build_xinput_dll dinput
build_xinput_dll dinput8
build_xinput_dll xinput1_1
build_xinput_dll xinput1_2
build_xinput_dll xinput1_3
build_xinput_dll xinput1_4
build_xinput_dll xinput9_1_0


#
#echo "Upload"
#sshpass -p 'root' scp -P 8022 ../../../../../output/32/* root@192.168.210.159:/data/data/com.termux/files/usr/glibc/opt/wine/3/wine/lib/wine/i386-windows
#sshpass -p 'root' scp -P 8022 ../../../../../output/64/* root@192.168.210.159:/data/data/com.termux/files/usr/glibc/opt/wine/3/wine/lib/wine/x86_64-windows




exit















