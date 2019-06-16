<?php
include 'dbconfig.php';

$question_id = mysqli_real_escape_string($conn, $_POST['question_id']);
$question = mysqli_real_escape_string($conn, $_POST['question']);
$answer = mysqli_real_escape_string($conn, $_POST['answer']);
$incorrect_one = mysqli_real_escape_string($conn, $_POST['incorrect_one']);
$incorrect_two = mysqli_real_escape_string($conn, $_POST['incorrect_two']);
$incorrect_three = mysqli_real_escape_string($conn, $_POST['incorrect_three']);

$conn->query("UPDATE psu_ecpd_questions SET question='$question' WHERE id = '$question_id'");

// delete all answers for this question
$conn->query("DELETE FROM psu_ecpd_answers WHERE question_id = '$question_id'");

// insert updated answers
$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$answer',1)");
$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$incorrect_one',0)");
$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$incorrect_two',0)");
$conn->query("INSERT INTO psu_ecpd_answers (question_id, answer, status) VALUES ('$question_id','$incorrect_three',0)");

echo "1";
?>