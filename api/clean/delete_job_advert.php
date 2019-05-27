<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "DELETE FROM psu_jobs WHERE id = '$id'";

$sql1 = "DELETE FROM psu_jobs_applications WHERE jobs_id = '$id'";

if ($conn->query($sql) === TRUE && $conn->query($sql1) === TRUE) {
    echo "1";
} else {
    echo "0";
}
?>