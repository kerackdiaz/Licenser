<?php

require_once 'config.php';

function connectDB() {
    try {
        $connection = new PDO('mysql:host=' . DB_HOST . ';dbname=' . DB_NAME, DB_USER, DB_PASSWORD);
        $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        return $connection;
    } catch (PDOException $e) {
        die('Error de conexión: ' . $e->getMessage());
    }
}

function generateLicenseKey() {
    $length = 16;
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $key = '';

    for ($i = 0; $i < $length; $i++) {
        $key .= $characters[rand(0, strlen($characters) - 1)];
    }

    return $key;
}
