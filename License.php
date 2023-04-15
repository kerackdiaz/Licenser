<?php

class License
{
    private $data;

    public function __construct($data)
    {
        $this->data = $data;
    }
    public function toArray()
    {
        return [
            ':user_name' => $this->data['user_name'],
            ':project_name' => $this->data['project_name'],
            ':project_url' => $this->data['project_url'],
            ':license_key' => $this->generateLicenseKey(),
            ':start_date' => $this->data['start_date'],
            ':end_date' => $this->data['end_date'],
            ':email' => $this->data['email'],
            ':license_type' => $this->data['license_type'],
            ':redirect_url' => $this->data['redirect_url'],
            ':is_lifetime' => $this->data['is_lifetime'],
            ':status' => 1,
            ':created_at' => date('Y-m-d H:i:s'),
            ':updated_at' => date('Y-m-d H:i:s'),
        ];
    }
    public function toUpdateArray()
    {
        return [
            'user_name' => $this->data['user_name'],
            'project_name' => $this->data['project_name'],
            'project_url' => $this->data['project_url'],
            'license_key' => isset($this->data['license_key']) ? $this->data['license_key'] : $this->generateLicenseKey(),
            'start_date' => $this->data['start_date'],
            'end_date' => $this->data['end_date'],
            'email' => $this->data['email'],
            'license_type' => $this->data['license_type'],
            'redirect_url' => $this->data['redirect_url'],
            'is_lifetime' => $this->data['is_lifetime'],
            'updated_at' => date('Y-m-d H:i:s'),
        ];
    }  

    private function generateLicenseKey()
    {
        return bin2hex(random_bytes(8)); // Genera una clave de licencia aleatoria de 32 caracteres
    }
    public function __get($property)
    {
        if (array_key_exists($property, $this->data)) {
            return $this->data[$property];
        }
        return null;
    }
}