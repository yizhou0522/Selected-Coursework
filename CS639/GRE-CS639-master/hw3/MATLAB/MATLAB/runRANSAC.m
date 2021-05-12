function [inliers_id, H] = runRANSAC(Xs, Xd, ransac_n, eps)
%RUNRANSAC
    num_pts = size(Xs, 1);
    pts_id = 1:num_pts;
    inliers_id = [];
    max=0;
    
    for iter = 1:ransac_n
        % ---------------------------
        % START ADDING YOUR CODE HERE
        % ---------------------------
        inds = randperm(num_pts,4);
        maxH = computeHomography(Xs(inds,:), Xd(inds,:));
        Xd_pred = applyHomography(maxH, Xs);
        dist = sqrt((Xd_pred(:,1)-Xd(:,1)).^2 + (Xd_pred(:,2)-Xd(:,2)).^2);
        M = sum(dist < eps);
        if M > max
          max = M;
          inliers_id = find(dist < eps);
          H = maxH;
        end
        % ---------------------------
        % END ADDING YOUR CODE HERE
        % ---------------------------
    end    
end
