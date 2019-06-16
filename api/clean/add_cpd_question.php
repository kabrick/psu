<?php
include 'dbconfig.php';

$ecpd_id = mysqli_real_escape_string($conn, $_POST['ecpd_id']);
$question = mysqli_real_escape_string($conn, $_POST['question']);
$answer = mysqli_real_escape_string($conn, $_POST['answer']);
$incorrect_one = mysqli_real_escape_string($conn, $_POST['incorrect_one']);
$incorrect_two = mysqli_real_escape_string($conn, $_POST['incorrect_two']);
$incorrect_three = mysqli_real_escape_string($conn, $_POST['incorrect_three']);

$conn->query("INSERT INTO psu_ecpd_questions (ecpd_id, question) VALUES ('$ecpd_id','$question')");
$question_id = $conn->insert_id;

$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$answer',1)");
$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$incorrect_one',0)");
$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$incorrect_two',0)");
$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$incorrect_three',0)");

echo "1";
?>