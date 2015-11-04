<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<title>Input Form</title>
</head>
<body>
	<form action="ActionController">
		<div class="container">
			<div class="column">
				<h2>Network 1</h2>
				<hr>
				<div class="dropdown">
					<select id="network1" name="network1">
						<option value="gplus1">Gplus 1</option>
						<option value="gplus2">Gplus 2</option>
						<option value="gplus3">Gplus 3</option>
					</select>
				</div>
			</div>
		</div>

		<div class="container">
			<div class="column">
				<h2>Network 2</h2>
				<hr>
				<div class="dropdown">
					<select id="network2" name="network2">
						<option value="twitter1">Twitter 1</option>
						<option value="twitter2">Twitter 2</option>
						<option value="twitter3">Twitter 3</option>
					</select>
				</div>
			</div>
		</div>

		<div class="container">
			<div class="column">
				<h2>Input Criteria</h2>
				<label for="Select Problem" class="control-label input-group">Select
					Algorithm</label>
				<div class="btn-group" id="button1">
					<label class="btn btn-default"> <input type="radio"
						class="toggle" name="problemChoice" value="1">HITS
					</label> <label class="btn btn-default"> <input type="radio"
						class="toggle" name="problemChoice" value="2">PageRank
					</label>

					<p id="demo3"></p>
				</div>
			</div>
		</div>
		<div class="container">
			<div class="column">
				<div>
					<br></br> <label>Enter the network for which you want to
						find the users</label> <input type="text" class="form-control"
						id="networkChoice" name="networkChoice" value=""
						style="width: 150px;">
				</div>
			</div>
		</div>

<div class="container">
			<div class="column">
		<div>
			<br></br> <label>Enter the index belonging to that network</label> <input
				type="text" class="form-control" id="index" value="" name="index"
				style="width: 150px;">
		</div>
		</div></div>

<div class="container">
			<div class="column">
		<div>
			<br></br> <label>Enter the top k users for which you want to
				get the data</label> <input type="text" class="form-control" id="topUsers"
				value="" name="topUsers" style="width: 150px;">
		</div></div></div>

<br>
<div class="container">
			<div class="column">
		<div>
			<input type="submit" class="btn btn-primary" id="dLabel3"
				onclick="readTextFile()" value="Calculate" />
		</div></div></div>

		<div id="div1"></div>
	</form>
	<script type="text/javascript">
		function readTextFile() {
			var xmlhttp;
			if (window.XMLHttpRequest) {
				xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
			} else {
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
			}
			xmlhttp.open("GET", "/ActionController.do", true);

			//When readyState is 4 then get the server output
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4) {

					var path = document.location.pathname;
					var dir1 = path.substring(path.indexOf('/', 0), path
							.lastIndexOf('/'));
					var file_name = "/data.txt";

					var file = dir1.concat(file_name);

					var rawFile = new XMLHttpRequest();
					rawFile.onreadystatechange = function() {
						if (rawFile.readyState == 4) {
							if (rawFile.status == 200 || rawFile.status == 0) {
								var allText = rawFile.responseText;
								var dev = document.getElementById('div1');

								/* dev.innerHTML = '</br><label for="comment">Result</label>' +
										'<textarea class="form-control" rows="5" id="comment">' + allText + '</textarea></br></br>'; */

								dev.innerHTML = '</br><label for="comment">Result</label>'
										+ '<textarea rows="5" id="comment">'
										+ allText + '</textarea></br></br>';
							}
						}
					}

					rawFile.open("GET", file, true);
					rawFile.send();
				} else {
					alert("Something went wrong. Please try again !!")
				}
			}
		}
		window.onload = function() {
			document.getElementById('text1').value = "";
			document.getElementById('text2').value = "";
		}
	</script>

</body>
</html>