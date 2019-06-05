<?php
include 'dbconfig.php';

$sql = "SELECT id, submitted_by, timestamp FROM psu_supervision_checklist_wholesale ORDER BY id DESC";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		$id = $row['submitted_by'];

		$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['timestamp'] = $row['timestamp'];
		$arr[$counter]['name'] = $result_assoc1['name'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>