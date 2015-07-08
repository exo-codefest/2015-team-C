band.on('change', function(period) {
  $('.data').each(function() {
    var $data = $(this);
    var $start = $data.find('.time').first();
    var $end = $data.find('.time').last();
    var $container = $data.closest('.container');
    var homeTz = parseInt($('.container').first().data('tz'));
    var tz = parseInt($container.data('tz'));

    var startDate = new Date();
    startDate.setTime(period[0] + startDate.getTimezoneOffset() * 60 * 1000 + (homeTz + tz) * 60 * 60 * 1000);
    var th = startDate.getHours();    
    //
    $start.find('.th').text(th < 10 ? '0' + th : th);
    $data.find('.date').first().html(startDate.toLocaleDateString());

    var endDate = new Date();
    endDate.setTime(period[1] + endDate.getTimezoneOffset() * 60 * 1000 + (homeTz + tz) * 60 * 60 * 1000);
    th = endDate.getHours();
    //
    $end.find('.th').text(th < 10 ? '0' + th : th);
    $data.find('.date').last().html(endDate.toLocaleDateString());
  });
});