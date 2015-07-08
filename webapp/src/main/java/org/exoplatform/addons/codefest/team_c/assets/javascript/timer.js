var Timer = function(options) {
  var moment = new Date();
  
  // default options
  var defOpts = {
    $container : $('.container').first(),
    tz : -1 * moment.getTimezoneOffset() / 60,
    homeTz : -1 * moment.getTimezoneOffset() / 60,
    date : moment,
    settings : {
      workingTime : [ [ 8, 17 ] ],
      overtime : [ [ 6, 7 ], [ 18, 21 ] ]
    }
  };

  this.options = $.extend(true, {}, defOpts, options);

  //nomalize date
  moment = this.options.date;
  moment.setTime(parseFloat($('.clientarea').data('starttime')) + 
      moment.getTimezoneOffset() * 60 * 1000 + (this.options.homeTz + this.options.tz) * 60 * 60 * 1000);
  
  // time type
  var isContainTime = function(timeArray, h) {
    var c = false;
    $.each(timeArray, function(idx, elem) {
      if (h >= elem[0] && h <= elem[1]) {
        return c = true;
      }
    });
    return c;
  };
  this.isWorkingTime = function(h) {
    return isContainTime(this.options.settings.workingTime, h);
  };
  this.isOverTime = function(h) {
    return isContainTime(this.options.settings.overtime, h);
  };

  // render
  this.render = function() {
    var first = new Date(this.options.date.getTime());
    
    var wDay = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    var month = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var li = '';
    for (var i = 0; i < 24; i++) {
      var style;
      if (first.getHours() == 0) {
        style = 'tod_c';
      } else if (this.isWorkingTime(first.getHours())) {
        style = 'tod_d';
      } else if (this.isOverTime(first.getHours())) {
        style = 'tod_m';
      } else {
        style = 'tod_n';
      }
      
      li += '<li class="' + style + '" data-time="' + (parseFloat($('.clientarea').data('starttime')) + i * 60 * 60 * 1000) + '">';
      if (first.getHours() == 0) {
        li += '<div>' + wDay[first.getDay()] + '</div>';
        li += '<b>' + month[first.getMonth()] + '</b>';
        li += '<i>' + first.getDate() + '</i>';
      } else {
        li += '<b>' + first.getHours() + '</b>';
      }
      li += '</li>';
      //
      first.setHours(first.getHours() + 1);
    }
    
    var $hourLine = this.options.$container.find('.hourline ._24');
    $hourLine.html(li);
  };
};

$(function() {
  var homeTz = $('.container').first().data('tz');
  
  $('.container').each(function() {
    var $container = $(this);
    var timer = new Timer({
      $container : $container,
      homeTz : parseInt(homeTz),
      tz : parseInt($container.data('tz')),
    });
    timer.render();
  });
});