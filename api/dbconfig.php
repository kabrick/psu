<?php 
/*error_reporting (E_ALL ^ E_NOTICE);
ini_set('display_errors', '1');

error_reporting(0);*/
$hostname_connection = "localhost";
$database_connection = "psucop_app";
$username_connection = "psucop_cop";
$password_connection = "psucop_@2018";

// $conn->error

$conn = new mysqli("$hostname_connection", "$username_connection", "$password_connection", "$database_connection");
?>