<?php
require_once 'functions.php';
require_once 'LicenseController.php';

if (isset($_GET['id'])) {
    $id = $_GET['id'];
    $licenseController = new LicenseController();
    $result = $licenseController->deleteProject($id);

    if ($result) {
        header('Location: index.php?message=Proyecto eliminado correctamente');
    } else {
        header('Location: index.php?message=Error al eliminar el proyecto');
    }
} else {
    header('Location: index.php?message=ID del proyecto no válido');
}
