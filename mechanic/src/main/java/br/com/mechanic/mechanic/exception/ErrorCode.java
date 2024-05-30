package br.com.mechanic.mechanic.exception;

public enum ErrorCode {
    DATABASE_ERROR,
    INTERNAL_ERROR,
    INVALID_FIELD,
    EMAIL_ALREADY_EXISTS,
    ERROR_CREATED_CLIENT,
    IDENTICAL_FIELDS,
    PHONE_NUMBER_ALREADY_EXISTS,
    CANNOT_CHANGE_STATUS,
    ERROR_PROVIDER_ACCOUNT_NOT_FOUND,
    ERROR_CREATED_ADDRESS,
    ERROR_PROVIDER_ACCOUNT_TYPE_NOT_FOUND,
    ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL,
    ERROR_CREATED_PHONE,
    ERROR_PROVIDER_PERSON_NOT_FOUND,
    ERROR_PROVIDER_ADDRESS_NOT_FOUND,
    VEHICLE_TYPE_EXCEPTION,
    ERROR_CREATED_VEHICLE_TYPE,
    ERROR_CREATED_PROVIDER_SERVICE,
    ERROR_PROVIDER_SERVICE_IN_USED,
    TYPE_SERVICE_NOT_FOUND,
    ERROR_CREATED_TYPE_SERVICE,
    ERROR_VEHICLE_TYPE;
}
