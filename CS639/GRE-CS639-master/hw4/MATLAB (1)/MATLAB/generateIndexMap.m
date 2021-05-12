function index_map = generateIndexMap(gray_stack, w_size)
    [H, W, N] = size(gray_stack);
    
    % Compute the focus measure -- the sum-modified laplacian
    %
    % horizontal Laplacian kernel
    Kx = [0.25 0 0.25;...
           1  -3   1; ...
          0.25 0 0.25];
    Ky = Kx';   % vertical version
    
    % horizontal and vertical Laplacian responses
    Lx = zeros(H, W, N);
    Ly = zeros(H, W, N);
    for n = 1:N
        I = im2double(gray_stack(:,:,n));
        Lx = imfilter(I, Kx, 'replicate', 'same', 'corr');
        Ly = imfilter(I, Ky, 'replicate', 'same', 'corr');
        SML = (abs(Lx) .^ 2) + (abs(Ly) .^ 2);
        H = fspecial('average',[2*w_size, 2*w_size]);
        focus_measure(:,:,n) = imfilter(SML, H, 'replicate');
    end
    
    % sum-modified Laplacian
    
    % can also use the absolute value itself
    % this is probably more well-known
    % SML = abs(Lx) + abs(Ly);
        
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % ADD YOUR CODE HERE
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    [~, index_map] = max(focus_measure, [], 3);
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
end