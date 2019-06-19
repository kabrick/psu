<?php
include 'dbconfig.php';

$cpd_id = $_GET['cpd_id'];
$user_id = $_GET['user_id'];
$score = $_GET['score'];
$timestamp = $_GET['timestamp'];

$conn->query("INSERT INTO psu_ecpd_exams (cpd_id, user_id, score, timestamp) VALUES ('$cpd_id','$user_id','$score', '$timestamp')");

echo "1";
?>