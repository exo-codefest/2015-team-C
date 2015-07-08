function diffImageOk(img)
{
    if(img.src.match('/team-c-addon-webapp/image/okblack.png')) img.src = "/team-c-addon-webapp/image/okgrey.png";
    else img.src = "/team-c-addon-webapp/image/okblack.png";
}

function diffImageNok(img)
{
    if(img.src.match('/team-c-addon-webapp/image/nokblack.png')) img.src = "/team-c-addon-webapp/image/nokgrey.png";
    else img.src = "/team-c-addon-webapp/image/nokblack.png";
}