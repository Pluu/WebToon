/**
 * MIT License
 * Copyright (c) 2024 Mehdi Janbarari (@janbarari)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function shadeColor(color, amount) {
  return '#' + color.replace(/^#/, '')
  .replace(/../g, color => ('0'+Math.min(255, Math.max(0, parseInt(color, 16) + amount))
  .toString(16))
  .substr(-2));
}

let resetFlag = 0
let randomColor = getRandomColor()

function getColor(single = false) {
  if(single) {
    return getRandomColor();
  }
  if(resetFlag > 1) {
    resetFlag = 0;
    randomColor = getRandomColor();
  }
  resetFlag++;
  return randomColor;
}

var isDarkMode = false

function toggleTheme() {
  var element = document.body;
  element.classList.toggle("dark-mode");

  if(!isDarkMode) {
    isDarkMode = true
    document.getElementById("toggleThemeButton").classList.remove('dark');
    document.getElementById("toggleThemeButton").classList.add('white');
    document.getElementById("toggleThemeButton").classList.remove('bi-moon-stars-fill');
    document.getElementById("toggleThemeButton").classList.add('bi-brightness-high-fill');
  } else {
    isDarkMode = false
    document.getElementById("toggleThemeButton").classList.remove('white');
    document.getElementById("toggleThemeButton").classList.add('dark');
    document.getElementById("toggleThemeButton").classList.remove('bi-brightness-high-fill');
    document.getElementById("toggleThemeButton").classList.add('bi-moon-stars-fill');
  }
}
