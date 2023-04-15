<?php
require_once 'functions.php';
require_once 'LicenseController.php';

$licenseController = new LicenseController();
$projects = $licenseController->getAllProjects();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de administración de licencias</title>
    <!-- Aquí puedes agregar estilos CSS y/o bibliotecas como Bootstrap -->
</head>
<body>
    <h1>Panel de administración de licencias</h1>
    <a href="../templates/create.php">Agregar proyecto</a>
    <table>
        <thead>
            <tr>
                <th>Nombre del usuario</th>
                <th>Nombre del proyecto</th>
                <th>URL del proyecto</th>
                <th>Clave de licencia</th>
                <th>Fecha de inicio</th>
                <th>Fecha de finalización</th>
                <th>Días restantes</th>
                <th>Correo electrónico</th>
                <th>Tipo de licencia</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <?php foreach ($projects as $project) : ?>
            <tr>
                <td><?= $project->user_name ?></td>
                <td><?= $project->project_name ?></td>
                <td><?= $project->project_url ?></td>
                <td><?= $project->license_key ?></td>
                <td><?= $project->start_date ?></td>
                <td><?= $project->end_date ?></td>
                <td>
                    <?php
                    if ($project->license_type != 'vitalicia' && !$project->is_lifetime) {
                        $remainingDays = (strtotime($project->end_date) - strtotime(date('Y-m-d'))) / 86400;
                        echo $remainingDays > 0 ? $remainingDays : 0;
                    } else {
                        echo '-';
                    }
                    ?>
                </td>
                <td><?= $project->email ?></td>
                <td><?= $project->license_type ?></td>
                <td><?= $project->status ? 'Activo' : 'Inactivo' ?></td>
                <td>
                    <a href="../templates/edit.php?id=<?= $project->id ?>">Editar</a>
                    <a href="toggle_status.php?id=<?= $project->id ?>"><?= $project->status ? 'Desactivar' : 'Activar' ?></a>
                    <a href="delete_project.php?id=<?= $project->id ?>" onclick="return confirm('¿Está seguro de que desea eliminar este proyecto?')">Eliminar</a>
                </td>
            </tr>
        <?php endforeach; ?>
        </tbody>
    </table>
</body>
</html>
