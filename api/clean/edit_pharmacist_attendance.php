<?php
include 'dbconfig.php';

// get sent data
$id = $_GET['id'];
$appraiser_name = $_GET['appraiser_name'];
$appraiser_title = $_GET['appraiser_title'];
$from_period = $_GET['from_period'];
$to_period = $_GET['to_period'];
$score_one = $_GET['score_one'];
$score_two = $_GET['score_two'];
$score_three = $_GET['score_three'];
$score_four = $_GET['score_four'];
$score_five = $_GET['score_five'];
$average_score = $_GET['average_score'];
$remarks = $_GET['remarks'];
$timestamp = $_GET['timestamp'];

//update values
$sql = "UPDATE psu_pharmacist_assessment SET appraiser_name='$appraiser_name',appraiser_title='$appraiser_title',from_period='$from_period',to_period='$to_period',score_one='$score_one',score_two='$score_two',score_three='$score_three',score_four='$score_four',score_five='$score_five',average_score='$average_score',remarks='$remarks',timestamp='$timestamp' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>