<%--
  Created by IntelliJ IDEA.
  User: Liza
  Date: 24.06.2021
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Store</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Righteous&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100;200;300;400;500&family=Nunito:wght@200;300;400&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <script type="text/javascript" src="resources/script.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script type="text/javascript" src="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css"
          rel="stylesheet" type="text/css">
    <link href="https://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css"
          rel="stylesheet" type="text/css">

    <link href="//fonts.googleapis.com/css?family=Roboto:300,400,500,700" rel="stylesheet" type="text/css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <style>
        * {
            padding: 0;
            margin: 0;
        }

        body {
            align-items: center;
            justify-content: center;
            background-color: #BFD7EA;
        }

        #titleDiv {
            height: 8.1vh;
            width: 100%;
            background-color: #4A4E69;
            -moz-column-count: 2; /* ?????? Firefox */
            -webkit-column-count: 2; /* ?????? Safari ?? Chrome */
            column-count: 2;
            display: table;
        }

        #div1 {
            display: table-cell;
            height: 8.1vh;
            width: 8.2vw;
        }

        #titleImage {
            height: 6.48vh;
            width: 3.284vw;
            margin: 5px;
            margin-left: 10px;
        }

        #div2 {
            display: table-cell;
            font-family: 'Righteous', cursive;
            font-size: 32px;
            color: white;
            text-align: center;
            vertical-align: middle;
            height: 8.1vh;
            width: 65.68vw;
            margin: 0 auto;
        }

        #div3 {
            display: table-cell;
            height: 8.1vh;
            width: 8.21vw;
            margin: 0 auto;
        }


        header {
            background: white;
            text-align: center;
            border-top: 1px solid black;
            border-bottom: 1px solid black;
        }

        header a {
            display: block;
            text-decoration: none;
            outline: none;
            transition: .3s ease-in-out;
        }

        nav {
            display: table;
            margin: 0 auto;
        }

        nav ul {
            list-style: none;
            margin: 0;
            padding: 0;
        }

        nav li a {
            margin-left: 1.642vw;
            margin-right: 0.821vw;
        }


        .topmenu {
            align-items: center;
            justify-content: center;
            text-align: center;
            padding-left: 6.568vw;
        }

        .topmenu:after {
            content: "";
            display: table;
            clear: both;
        }

        .topmenu > li {
            width: 25%;
            float: left;
            position: relative;
            font-family: 'Montserrat', sans-serif;
            font-size: 20px;
            font-weight: 400;
        }

        .topmenu > li > a {
            text-transform: uppercase;
            font-size: 14px;
            font-weight: bold;
            color: #404040;
            padding: 15px 30px;
        }

        .topmenu li a:hover {
            color: #D5B45B;
        }

        .submenu-link:after {
            content: "\f107";
            font-family: "FontAwesome";
            color: inherit;
            margin-left: 10px;
        }

        .submenu {
            background: #273037;
            position: absolute;
            left: 0;
            top: 100%;
            z-index: 5;
            width: 14.778325vw;
            opacity: 0;
            transform: scaleY(0);
            transform-origin: 0 0;
            transition: .5s ease-in-out;
        }

        .submenu a {
            color: white;
            text-align: left;
            padding: 12px 15px;
            font-size: 13px;
            border-bottom: 1px solid rgba(255, 255, 255, .1);
        }

        .submenu li:last-child a {
            border-bottom: none;
        }

        .topmenu > li:hover .submenu {
            opacity: 1;
            transform: scaleY(1);
        }


        #mainDiv {
            height: 64.82982vh;
            width: 82.1vw;
            margin: 0 auto;
            margin-top: 6.483vh;
            -moz-column-count: 2; /* ?????? Firefox */
            -webkit-column-count: 2; /* ?????? Safari ?? Chrome */
            column-count: 2;
        }

        #filters {
            float: left;
            width: 30vw;
            height: 60.6vh;
            padding: 10px;
            border-radius: 5px;
            border: 2px solid #404040;
        }

        #result {
            float: right;
            width: 49.261vw;
            height: 64vh;
        }

        .amount, .price {
            -moz-column-count: 3; /* ?????? Firefox */
            -webkit-column-count: 3; /* ?????? Safari ?? Chrome */
            column-count: 3;
            width: 45vw;
        }

        .amountLabel, .priceLabel {
            float: left;
            margin-top: 3.241491vh;
            font-family: 'Montserrat', sans-serif;
            font-weight: 500;
            font-size: 15px;
            color: #404040;
        }

        .amountFrom, .amountTo, .priceFrom, .priceTo {
            float: left;
        }

        .allProducts {
            width: 41vw;
            height: 60vh;
            margin: 15px;
            padding: 10px;
            border-radius: 5px;
            border: 2px solid #404040;
            background-color: white;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
            font-size: 15px;
            color: #404040;
        }

        .form-rowS {
            position: relative;
            margin-bottom: 3.24vh;
        }

        .form-rowS input {
            display: block;
            width: 21.34647vw;
            padding: 0 10px;
            line-height: 6.48vh;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
            font-size: 15px;
            background: none;
            border-width: 0;
            border-bottom: 2px solid #404040;
            transition: all 0.2s ease;
        }

        .form-rowS label {
            position: absolute;
            left: 13px;
            color: #9d959d;
            font-size: 20px;
            font-weight: 300;
            transform: translateY(-35px);
            transition: all 0.2s ease;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
            font-size: 15px;
        }

        .form-rowS input:focus {
            outline: 0;
            border-color: #F77A52;
        }

        .form-rowS input:focus + label, .form-rowS input:valid + label {
            transform: translateY(-60px);
            margin-left: -14px;
            font-size: 14px;
            font-weight: 400;
            outline: 0;
            border-color: #F77A52;
            color: #F77A52;
        }


        .form-row {
            position: relative;
            margin-bottom: 2.24vh;
        }

        .form-row input {
            display: block;
            width: 5.747vw;
            padding: 0 10px;
            line-height: 6.48vh;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
            font-size: 15px;
            background: none;
            border-width: 0;
            border-bottom: 2px solid #404040;
            transition: all 0.2s ease;
        }

        .form-row label {
            position: absolute;
            left: 13px;
            color: #9d959d;
            font-size: 20px;
            font-weight: 300;
            transform: translateY(-35px);
            transition: all 0.2s ease;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
            font-size: 15px;
        }

        .form-row input:focus {
            outline: 0;
            border-color: #F77A52;
        }

        .form-row input:focus + label, .form-row input:valid + label {
            transform: translateY(-60px);
            margin-left: -14px;
            font-size: 14px;
            font-weight: 400;
            outline: 0;
            border-color: #F77A52;
            color: #F77A52;
        }


        #bottom {
            height: 8.1vh;
        }

        .buttonOk {
            text-align: left;
            margin-left: -20px;
        }

        .floating-button {
            text-decoration: none;
            display: inline-block;
            width: 11.5vw;
            height: 7.3vh;
            line-height: 7.3vh;
            border-radius: 45px;
            margin: 10px 20px;
            font-family: 'Montserrat', sans-serif;
            font-size: 15px;
            text-transform: uppercase;
            text-align: center;
            letter-spacing: 3px;
            font-weight: 600;
            color: #524f4e;
            background: white;
            box-shadow: 0 8px 15px rgba(0, 0, 0, .1);
            transition: .3s;
        }

        .floating-button:hover {
            background: #D5B45B;
            box-shadow: 0 15px 20px rgba(213, 180, 91, .4);
            color: white;
            transform: translateY(-7px);
        }
    </style>
</head>

<body>
<div id="titleDiv">
    <div id="div1"><img id="titleImage" src="resources/logo.png" align="center"/></div>
    <div id="div2">STORE</div>
    <div id="div3"></div>
</div>

<div id="menuDiv">
    <header>
        <nav>
            <ul class="topmenu">
                <li><a href="" class="submenu-link">Groups</a>
                    <ul class="submenu">
                        <li><a href="http://localhost:8080/Store">Create</a></li>
                        <li><a href="http://localhost:8080/Store/update">Update</a></li>
                        <li><a href="http://localhost:8080/Store/delete">Delete</a></li>
                    </ul>
                </li>
                <li><a href="" class="submenu-link">Products</a>
                    <ul class="submenu">
                        <li><a href="http://localhost:8080/Store/CreateProduct">Create</a></li>
                        <li><a href="http://localhost:8080/Store/updateProduct">Update</a></li>
                        <li><a href="http://localhost:8080/Store/deleteProduct">Delete</a></li>
                        <li><a href="http://localhost:8080/Store/addToProduct">Add</a></li>
                        <li><a href="http://localhost:8080/Store/writeOffFromProduct">Write off</a></li>
                        <li><a href="http://localhost:8080/Store/search">Search</a></li>
                    </ul>
                </li>
                <li><a href="" class="submenu-link">Statistics</a>
                    <ul class="submenu">
                        <li><a href="http://localhost:8080/Store/allProducts">All products</a></li>
                        <li><a href="http://localhost:8080/Store/allProductsInGroup">All products in group</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </header>

</div>

<div id="mainDiv">
    <div id="filters">
        <div class="form-rowS">
            <input type="text" id="name" required autocomplete="off"><label for="name">Name</label>
        </div>
        <div class="form-rowS">
            <input type="text" id="description" required autocomplete="off"><label for="description">Description</label>
        </div>
        <div class="form-rowS">
            <input type="text" id="manufacturer" required autocomplete="off"><label for="manufacturer">Manufacturer</label>
        </div>

        <div class="amount">
            <div class="amountLabel">Amount&nbsp; &nbsp;</div>
            <div class="amountFrom">
                <div class="form-row"><input type="number" id="amountFromId" required autocomplete="off"><label
                        for="amountFromId">from</label></div>
            </div>
            <div class="amountTo">
                <div class="form-row"><input type="number" id="amountToId" required autocomplete="off"><label
                        for="amountToId">to</label></div>
            </div>
        </div>

        <div class="price">
            <div class="priceLabel">Price&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
            <div class="priceFrom">
                <div class="form-row"><input type="number" id="priceFromId" required autocomplete="off"><label
                        for="priceFromId">from</label></div>
            </div>
            <div class="priceTo">
                <div class="form-row"><input type="number" id="priceToId" required autocomplete="off"><label
                        for="priceToId">to</label></div>
            </div>
        </div>

        <div class="buttonOk"><!--<input type="submit" class="superbutton" value="Ok">-->
            <a href="" class="floating-button" onclick="searchProducts()">Search</a>
        </div>
    </div>

    <div id="result">
        <div class="allProducts">
        </div>
    </div>

</div>

<div id="bottom">
</div>

</body>

</html>