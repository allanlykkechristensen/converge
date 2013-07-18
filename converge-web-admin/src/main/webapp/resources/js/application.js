/* 
 * Copyright (C) 2012 Interactive Media Management
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


$(document).ready(function() {
    
    // Popovers
    $("a[rel=popover]").popover().click(function(e) {
        e.preventDefault()
    })
    $(".show-popover").popover();

    // Tooltips
    $("a[rel=tooltip]").tooltip();
    $('.btn-workflow-state-option').tooltip();

    // Page menu
    $("ul.nav-list li").click(function(){
        $("ul.nav-list li").removeClass("active");
        $(this).addClass("active");
        
        if ($(this).attr("title") != undefined) {
            var title = $(this).attr("title");
            $('h2#pageTitle').html(title);
        }
    })
    
} );
