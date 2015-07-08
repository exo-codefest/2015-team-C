var band = {
  listener : {},
    
  operation : null,
  
  start : 0,
  
  end : 0,
  
  choices : [],
  
  on : function(name, callback) {
    if (!band.listener.name) {
      band.listener.name = [];
    }
    band.listener.name.push(callback);
  },
  
  off : function() {
    band.listener = {};
  },
  
  trigger : function(name, data) {
    if (band.listener.name) {
      $.each(band.listener.name, function(idx, elem) {
        elem.call(band, data);
      });
    }
  },
  
  registerEvent : function() {
    var $glass = $('#glass');
    $glass.on('mouseenter', function() {
      if (band.operation) {      
        band.operation.call(this);
      }    
    });

    $glass.on('click', function() {
      if (!band.operation) {
        band.operation = band.moveBand;
      } else if (band.operation == band.moveBand) {
        band.operation = band.resize;
      } else {
        $glass.off('mousemove');
        band.operation = null;
        return;
      }
      band.operation.call(this);
    });

    $glass.on('mouseleave', function() {
      $(this).off('mousemove');
    });
  },
  
  moveBand : function() {
    var $hourLine = $('.hourline ul').first();
    var $items = $hourLine.find('li');
    var $band = $('.band');  
    
    $(this).off('mousemove').on('mousemove', function(e) {
      var mouseX = e.pageX;
      var delta = mouseX - $hourLine.offset().left;

      //
      var multi = Math.round(delta / 29);
      if (multi < $items.length
          && $($items.get(multi)).offset().left - 3 + $band.width() < $items.last().offset().left
              + $items.last().width() + 5) {
        $band.offset({
          left : $($items.get(multi)).offset().left - 3
        });
        //
        var time = parseFloat($($items.get(multi)).data('time'));
        var change = time - band.start;
        band.start = time;
        band.end =  band.end + change;
        band.trigger('change', [band.start, band.end]);
      }
    });
  },

  resize : function() {
    var $hourLine = $('.hourline ul').first();
    var $items = $hourLine.find('li');
    var $band = $('.band');  
    
    $(this).off('mousemove').on('mousemove', function(e) {
      var mouseX = e.pageX;
      var delta = mouseX - $hourLine.offset().left;

      //
      var multi = Math.ceil(delta / 29);
      if (multi <= $items.length) {
        var idx = multi == $items.length ? multi - 1 : multi;
        var width = $($items.get(idx)).offset().left - $band.offset().left - 2;
        if (multi == $items.length) {
          width += $($items.get(idx)).width() + 3;
        }
        if (width > 10) {
          $band.width(width);
          //
          var time = parseFloat($($items.get(idx)).data('time'));
          band.end = time;
          band.trigger('change', [band.start, band.end]);
        }
      }
    });
  }
};

$(function() {  
  band.start = parseFloat($('.clientarea').data('starttime'));
  band.end = parseFloat($('.clientarea').data('endtime')); 
  band.registerEvent();
});