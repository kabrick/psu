<?php 
//error_reporting (E_ALL ^ E_NOTICE);
//ini_set('display_errors', '1');

//error_reporting(0);
$hostname_connection = "localhost";
$database_connection = "id9172434_psucop_psu";
$username_connection = "id9172434_root";
$password_connection = "root@1";

$conn = new mysqli("$hostname_connection", "$username_connection", "$password_connection", "$database_connection");
?>