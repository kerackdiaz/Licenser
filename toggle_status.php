<?php
require_once 'functions.php';
require_once 'LicenseController.php';

if (isset($_GET['id'])) {
    $id = $_GET['id'];
    $licenseController = new LicenseController();
    $result = $licenseController->toggleProjectStatus($id);

    if ($result) {
        header('Location: index.php?message=Estado del proyecto actualizado correctamente');
    } else {
        header('Location: index.php?message=Error al actualizar el estado del proyecto');
    }
} else {
    header('Location: index.php?message=ID del proyecto no válido');
}
