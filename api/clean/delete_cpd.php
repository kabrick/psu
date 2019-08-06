<?php
include 'dbconfig.php';

$id = $_GET['id'];

$conn->query("DELETE FROM psu_ecpd WHERE id = '$id'");
$conn->query("DELETE FROM psu_ecpd_questions WHERE ecpd_id = '$id'");
$conn->query("DELETE FROM psu_ecpd_answers WHERE ecpd_id = '$id'");

echo "1";
?>