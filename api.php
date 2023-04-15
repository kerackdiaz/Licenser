<?php

require_once 'functions.php';
require_once 'LicenseController.php';

header('Content-Type: application/json');

// Verificar que se haya enviado la clave de licencia en la solicitud
if (!isset($_GET['license_key'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Se requiere la clave de licencia']);
    exit;
}

$licenseKey = $_GET['license_key'];

$licenseController = new LicenseController();

// Buscar la licencia en la base de datos
$license = $licenseController->getLicenseByKey($licenseKey);

// Si no se encuentra la licencia, devolver un error
if (!$license) {
    http_response_code(404);
    echo json_encode(['error' => 'Licencia no encontrada']);
    exit;
}

// Verificar si la licencia es válida
$isValid = $licenseController->isLicenseValid($license);

// Calcular días restantes
$remainingDays = '-';
if ($license->license_type != 'vitalicia' && !$license->is_lifetime) {
    $remainingDays = (strtotime($license->end_date) - strtotime(date('Y-m-d'))) / 86400;
    $remainingDays = $remainingDays > 0 ? $remainingDays : 0;
}

// Devolver el estado de la licencia, el tipo de licencia y los días restantes
echo json_encode([
    'is_valid' => $isValid,
    'license_type' => $license->license_type,
    'remaining_days' => $remainingDays
]);
