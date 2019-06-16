<?php
include 'dbconfig.php';

$cpd_id = $_GET['cpd_id'];
$user_id = $_GET['user_id'];
$score = $_GET['score'];

$conn->query("INSERT INTO psu_ecpd_exams (cpd_id, user_id, score) VALUES ('$cpd_id','$user_id','$score')");

echo "1";
?>