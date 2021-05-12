function refocusApp(rgb_stack, depth_map)
height = size(rgb_stack,1);
width = size(rgb_stack,2);
canvas=uint8(rgb_stack(:,:,1:3));
imshow(canvas);
try
    [x,y] = ginput(1);
    catch
% solve the exit error
       return
end
while x > 0 && x < width &&y >0 &&y < height
    try
    [x,y] = ginput(1);
    catch
% solve the exit error
       return
    end
    best_idx = depth_map(round(y), round(x));
    new_idx = (best_idx-1)*3+1;
    imshow(uint8(rgb_stack(:,:,new_idx:new_idx+2)));
end
end

