require recipes-kernel/linux/linux-mainline-common.inc

SUMMARY = "Milk-V Duo mainline kernel recipe"

LINUX_VERSION ?= "5.10+"
KERNEL_VERSION_SANITY_SKIP="1"
PV = "${LINUX_VERSION}+git${SRCPV}"

BRANCH = "linux-5.10.y"
SRCREV = "v5.10.215"
SRCPV = "${@bb.fetch2.get_srcrev(d)}"
SRC_URI = " \
	git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;branch=${BRANCH} \
	file://0001-linux-port-cvitek-asic-chips.patch \
	file://0002-add-support-for-milkv-duo-board.patch \
	file://0003-add-dts.patch \
	file://rpmsg-core-add-API-to-get-MTU.patch \
	file://tty-add-rpmsg-driver.patch \
	file://add-CV180x-mailbox-driver.patch \
	file://add-CV180x-remoteproc-driver.patch \
	file://dts-switch-to-single-cell-address.patch \
	file://dts-add-remoteproc-support.patch \
	file://dts-add-pinctrl.patch \
	file://dts-refactor-milkv-duo.patch \
	file://dts-add-milkv-duo256m-and-milkv-duos.patch \
	file://v5_10_y_defconfig \
	file://multi.its \
"

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-milkv-duo:"

KERNEL_DEVICETREE:milkv-duo ?= "cvitek/cv1800b_milkv_duo_sd.dtb"
KERNEL_DEVICETREE:milkv-duos ?= "cvitek/sg2000_milkv_duos_sd.dtb"
KERNEL_DEVICETREE:milkv-duo256m ?= "cvitek/sg2002_milkv_duo256m_sd.dtb"

DEPENDS = "u-boot-mkimage-native dtc-native"

do_deploy[depends] = "milkv-duo-fsbl:do_deploy"

do_deploy:append() {
	cp ${B}/arch/riscv/boot/Image.gz ${B}
	cp ${WORKDIR}/multi.its ${B}
	mkimage -f ${B}/multi.its ${B}/uImage.fit
	install -m 744 ${B}/uImage.fit ${DEPLOYDIR}
	install -m 744 ${B}/arch/riscv/boot/dts/${KERNEL_DEVICETREE} ${DEPLOYDIR}/default.dtb
}

COMPATIBLE_MACHINE = "milkv-(duo|duo256m|duos)"
