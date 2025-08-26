package com.naipy.alpha.modules.utils;

public class UniversalSerialVersion {

    private UniversalSerialVersion() {
        throw new UnsupportedOperationException("This class cannot be instanced");
    }

    // Variação da classe 0000 - 0000 sequencia de modelos

    public static final long USER_SERIAL_VERSION_UID = 10000001L;
    public static final long PRODUCT_SERIAL_VERSION_UID = 10000002L;
    public static final long ORDER_PRODUCT_SERIAL_VERSION_UID = 10000003L;
    public static final long ORDER_PRODUCT_PK_SERIAL_VERSION_UID = 10000004L;
    public static final long ORDER_SERIAL_VERSION_UID = 10000005L;
    public static final long PAYMENT_SERIAL_VERSION_UID = 10000006L;
    public static final long CATEGORY_SERIAL_VERSION_UID = 10000007L;
    public static final long TOKEN_SERIAL_VERSION_UID = 10000008L;
    public static final long ADDRESS_SERIAL_VERSION_UID = 10000009L;
    public static final long USER_ADDRESS_SERIAL_VERSION_UID = 10000010L;

}
