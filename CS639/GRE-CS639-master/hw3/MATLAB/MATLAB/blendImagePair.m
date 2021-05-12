function out_img = blendImagePair(wrapped_imgs, masks, wrapped_imgd, maskd, mode)
%BLENDIMAGEPAIR
    % Convention -- suffix s: source, suffix d: destination
    Hs = size(wrapped_imgs, 1);
    Ws = size(wrapped_imgs, 2);
    Cs = size(wrapped_imgs, 3);
    
    Hd = size(wrapped_imgd, 1);
    Wd = size(wrapped_imgd, 2);
    Cd = size(wrapped_imgd, 3);
    
    assert((Hs == Hd) & (Ws == Wd) & (Cs == Cd));
    
    % make sure both images are the same type, and are a known type too.
    assert(isa(wrapped_imgs, 'uint8') | isa(wrapped_imgs, 'single') | isa(wrapped_imgs, 'double'));
    
    % 'double' makes sure we do not have any overflow/underflow problems
    % we will convert out_img to the right type at the end
    out_img = zeros(Hs, Ws, Cs, 'double');
    input_type = class(wrapped_imgs);

    % convert to 'double' to avoid overflow/underflow when multiplying with
    % the weighted mask
    wrapped_imgs = double(wrapped_imgs);
    wrapped_imgd = double(wrapped_imgd);
    
    binary_mask_s = masks > 0;
    binary_mask_d = maskd > 0;
    
    for c = 1:Cs
        channel_out = zeros(Hs, Ws, 'double');
        S = wrapped_imgs(:,:,c);
        D = wrapped_imgd(:,:,c);
        if strcmp(mode, 'overlay')
            % s first, then d overwrites s wherever there is overlap.
            channel_out(binary_mask_s) = S(binary_mask_s);
            channel_out(binary_mask_d) = D(binary_mask_d);
            out_img(:,:,c) = channel_out;
        elseif strcmp(mode, 'blend')
            % ---------------------------
            % ADD YOUR CODE HERE
            % ---------------------------
            % 
            % you need to compute the weighted masks (for src and dest) using
            % bwdist, and use them to form the output image.
            % 
            w_masks = double(rescale(bwdist(masks==0, 'euclidean')));
            w_maskd = double(rescale(bwdist(maskd==0, 'euclidean')));
            % set mask to the correct value
            w_masks = cat(3, w_masks, w_masks, w_masks);
            w_maskd = cat(3, w_maskd, w_maskd, w_maskd);
            out_img =(wrapped_imgs.*w_masks + wrapped_imgd.*w_maskd)./(w_masks + w_maskd);
            out_img(isnan(out_img)) = 0;

        end
      
    end
    
    % convert out_img to right type
    if strcmp(input_type, 'uint8')
        % clip to 0-255 range after rounding
        out_img = uint8(max(0, min(255, round(out_img))));
    elseif strcmp(input_type, 'single')
        out_img = single(out_img);
    end
end
