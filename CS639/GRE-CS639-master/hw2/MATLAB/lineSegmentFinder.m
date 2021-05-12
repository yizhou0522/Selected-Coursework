function cropped_line_img = lineSegmentFinder(orig_img, hough_img, hough_threshold)
    step=0.1;
    edge_img = im2double(edge(orig_img, 'canny',  0.03));
    [thetas, rhos] = size(hough_img);
    img = bwmorph(edge_img, 'dilate', 1);
    [rows, cols] = size(orig_img);
    max_rho = sqrt(rows^2 + cols^2);
    fh = figure; 
    imshow(orig_img);
    hold on;    
    for theta = 1 : thetas
        for rho = 1 : rhos
            if hough_img(theta, rho) > hough_threshold               
                rho_val = max_rho * (rho / (rhos / 2) - 1);                
                x = 1;
                theta_val = theta * 180 / thetas;
                while x <= rows
                    y = (x * sind(theta_val)/cosd(theta_val) + rho_val/cosd(theta_val));
                    y_round = round(y);
                    x_prune = [];
                    y_prune = [];
                    if y_round > 0 && y_round < cols && img(round(x), y_round) > 0
                        x_prune(1) = x;
                        y_prune(1) = y;
                        x = x + step;
                        y = (x * sind(theta_val)/cosd(theta_val) + rho_val/cosd(theta_val));
                        y_round = round(y);
                        while x <= rows && y_round > 0 && y_round < cols
                            if img(round(x), y_round) <= 0
                                step=0.1;
                                break;
                            else                             
                              x = x + step;
                              y = (x * sind(theta_val)/cosd(theta_val) + rho_val/cosd(theta_val));
                              y_round = round(y);
                            end
                        end
                        x_prune(2) = x;
                        y_prune(2) = y;
                        line(y_prune,x_prune, 'LineWidth',2, 'Color', [0, 0, 1]);
                    end
                    x = x + step;
                end
            end
        end
    end     
    cropped_line_img = saveAnnotatedImg(fh);
end

function annotated_img = saveAnnotatedImg(fh)
    figure(fh); % Shift the focus back to the figure fh

    % The figure needs to be undocked
    set(fh, 'WindowStyle', 'normal');

    % The following two lines just to make the figure true size to the
    % displayed image. The reason will become clear later.
    img = getimage(fh);
    truesize(fh, [size(img, 1), size(img, 2)]);

    % getframe does a screen capture of the figure window, as a result, the
    % displayed figure has to be in true size. 
    frame = getframe(fh);
    frame = getframe(fh);
    pause(0.5); 
    % Because getframe tries to perform a screen capture. it somehow 
    % has some platform depend issues. we should calling
    % getframe twice in a row and adding a pause afterwards make getframe work
    % as expected. This is just a walkaround. 
    annotated_img = frame.cdata;
end