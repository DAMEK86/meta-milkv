require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS:append = " xxd-native bc-native"

PROVIDES += "u-boot"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

SRC_URI = "git://github.com/milkv-duo/milkv-duo-u-boot;protocol=https;branch=duo-64mb \
           file://uboot-milkv-duo.env \
           file://uEnv-milkv-duo.txt \
           file://milkv-duo-support-files.patch \
           file://0001-skip-cvitek-board-init.patch \
           file://0002-Add-milkv-boards-support.patch \
           "

SRCREV = "4345a29c08e67044021f74139b4ff307019e9932"

COMPATIBLE_MACHINE = "milkv-(duo|duo256m|duos)"

UBOOT_MACHINE = "${MACHINE}_defconfig"

do_configure:prepend () {
    if [ -f "${WORKDIR}/uboot-milkv-duo.env" ]; then
        cp ${WORKDIR}/uboot-milkv-duo.env ${S}/include/milkv-duo.env
    fi
}

do_deploy:append () {
    if [ -f "${WORKDIR}/uEnv-milkv-duo.txt" ]; then
        cp ${WORKDIR}/uEnv-milkv-duo.txt ${DEPLOYDIR}/uEnv.txt
    fi
    install -m 0644 ${B}/u-boot.dtb ${DEPLOYDIR}
}
