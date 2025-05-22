import React from 'react';

function addParamsToUri(path, params) {
    const searchParams = new URLSearchParams();

    Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== "") {
            searchParams.append(key, value.toString());
        }
    });

    const queryString = searchParams.toString();

    return queryString ? `${path}?${queryString}` : path;
}
export default addParamsToUri;
