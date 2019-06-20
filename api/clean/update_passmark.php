<?php
include 'dbconfig.php';

$mark = mysqli_real_escape_string($conn, $_POST['mark']);

//update values
$sql = "UPDATE psu_settings SET test_pass_mark='$mark' WHERE id = 1";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>