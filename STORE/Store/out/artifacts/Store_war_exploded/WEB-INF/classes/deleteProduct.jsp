<%--
  Created by IntelliJ IDEA.
  User: Liza
  Date: 24.06.2021
  Time: 08:04
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
            -moz-column-count: 2; /* Для Firefox */
            -webkit-column-count: 2; /* Для Safari и Chrome */
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
        }

        .groupSelection {
            margin-bottom: 3.24vh;
        }

        .selection {
            border-radius: 5px;
            border: 2px solid #404040;
            background-color: transparent;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
            font-size: 15px;
            color: #9d959d;
            padding: 10px;
        }

        .selection:focus {
            outline: none;
        }


        #bottom {
            height: 8.1vh;
        }

        .buttonOk {
            text-align: center;
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
                        <li><a href="http://localhost:8080/Store/createProduct">Create</a></li>
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
    <div class="groupSelection">
        <select class="selection">
            <option disabled selected>Choose the group</option>
            <option value="group1">Group 1</option>
        </select>
    </div>

    <div class="groupSelection">
        <select class="selection">
            <option disabled selected>Choose the product</option>
            <option value="group1">Product 1</option>
        </select>
    </div>

</div>

<div id="bottom">
    <div class="buttonOk"><!--<input type="submit" class="superbutton" value="Ok">-->
        <a href="" class="floating-button" onclick="deleteGroup()">Delete</a>
    </div>
</div>

</body>

</html>