<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "DELETE FROM psu_ecpd_questions WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
	$conn->query("DELETE FROM psu_ecpd_answers WHERE question_id = '$id'");
    echo "1";
} else {
    echo "0";
}
?>