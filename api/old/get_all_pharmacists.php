<?php
include 'dbconfig.php';

$sql = "SELECT DISTINCT(a.psu_id), a.name FROM psu_admin a, psu_attendance_log b WHERE a.type = 'pharmacists' AND a.psu_id = b.psu_id";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row[] = $result->fetch_assoc()) {
 		$json = json_encode($row);
 	}
} else {
	//
}

echo $json;

?>