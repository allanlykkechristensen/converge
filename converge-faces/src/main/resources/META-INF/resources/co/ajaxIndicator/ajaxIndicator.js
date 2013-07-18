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

if (!window["ajaxIndicator"]) {
    var ajaxIndicator = {};
}

ajaxIndicator.onStatusChange = function onStatusChange(data) {
        
    var status = data.status;
    var componentId = ajaxIndicator[data.source.id];
    
    // Determine if component should be listened for, otherwise stop
    if (!componentId) {  
        return;
    }

    var targetId = ajaxIndicator["T-" + data.source.id];


    $t = $("#" + targetId);


    $("#" + escape(componentId) + "\\:overlay").css({
        opacity : 0.5,
        top     : $t.offset().top,
        width   : $t.outerWidth(),
        height  : $t.outerHeight(),
        left    : $t.offset().left
    });

    $("#" + escape(componentId) + "\\:overlay img").css({
        top  : ($t.height() / 2),
        left : ($t.width() / 2)
    });

    var element = document.getElementById(componentId + ":overlay");
    console.log(status);
    if (status === "begin") { // turn on busy indicator
        element.style.display = "inline";
        $("#" + escape(componentId) + "\\:overlay").fadeIn();
    } else {  // turn off busy indicator, on either "complete" or "success"
        element.style.display = "none";
        $("#" + escape(componentId) + "\\:overlay").fadeOut();
    }
};
   
jsf.ajax.addOnEvent(ajaxIndicator.onStatusChange);
   
ajaxIndicator.init =  function init(componentId, targetId, forValue) {
    ajaxIndicator[forValue] = componentId;
    ajaxIndicator["T-" + forValue] = targetId;
};
