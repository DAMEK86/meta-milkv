#@TYPE: Include
#@NAME: milkv-common
#@DESCRIPTION: Common configuration for Milk-V Duo boards

require conf/machine/include/riscv/tune-riscv.inc

MACHINE_FEATURES = "screen keyboard ext2 ext3 serial"

KERNEL_CLASSES = "kernel"
KERNEL_IMAGETYPE = "Image.gz"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-milkv-duo-dev"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-milkv"

PREFERRED_VERSION_openocd-native = "riscv"
PREFERRED_VERSION_openocd = "riscv"

SERIAL_CONSOLES = "115200;ttyS0"

IMAGE_FSTYPES += "wic.gz wic.bmap ext4"

## Do not update fstab file when using wic images
WIC_CREATE_EXTRA_ARGS ?= "--no-fstab-update"

# opensbi
RISCV_SBI_PLAT = "generic"

EXTRA_IMAGEDEPENDS += "milkv-duo-fsbl"

IMAGE_BOOT_FILES ?= " \
                    default.dtb \
                    fip.bin \
                    uEnv.txt \
                    uImage.fit \
                    "

WKS_FILE ?= "milkv-duo.wks"

##============================================
# constants

CVIMMAP_DRAM_BASE = "0x80000000"

CVIMMAP_DRAM_SIZE = "0x20000000"
CVIMMAP_FREERTOS_SIZE = "0x200000"
CVIMMAP_ION_SIZE = "0"

python () {
    ion_size = int(d.getVar("CVIMMAP_ION_SIZE", True), 16)
    ram_base = int(d.getVar("CVIMMAP_DRAM_BASE", True), 16)
    ram_size = int(d.getVar("CVIMMAP_DRAM_SIZE", True), 16)
    rtos_size = int(d.getVar("CVIMMAP_FREERTOS_SIZE", True), 16)

    start_addr = ram_base + ram_size - rtos_size

    d.setVar("CVIMMAP_FSBL_C906L_START_ADDR", hex(start_addr))
    d.setVar("CVIMMAP_ION_ADDR", hex(start_addr - ion_size))
    d.setVar("CVIMMAP_KERNEL_MEMORY_SIZE", hex(ram_size - rtos_size))
}
