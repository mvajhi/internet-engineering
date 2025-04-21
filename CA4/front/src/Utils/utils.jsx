import React from 'react';

const addParamsToUri = function (uri, params) {
    const url = new URL(uri); // Create a URL object
    Object.keys(params).forEach(key => {
        url.searchParams.append(key, params[key]); // Add each key-value pair to the URL's search parameters
    });
    return url.toString(); // Return the updated URI
}

export default addParamsToUri;
