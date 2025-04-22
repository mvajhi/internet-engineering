import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";

// ImageCell Component
const ImageCell = ({ alt, src }) => {
  return (
    <td className="align-middle">
      <img alt={alt} className="img-fluid" src={src} />
    </td>
  );
};

// TextCell Component
const TextCell = ({ text, customRender, item }) => {
  const content = customRender ? customRender(item) : text;

  return (
    <td className="align-middle">
      <div className="text-sm text-dark">{content}</div>
    </td>
  );
};

// LinkCell Component
const LinkCell = ({ to, text }) => {
  return (
    <td className="align-middle">
      <ul className="pagination m-0 justify-content-center align-items-center">
        <li className="page-item active" aria-current="page">
          <Link to={to} className="page-link text-dark border-0 rounded-3 px-4">
            {text}
          </Link>
        </li>
      </ul>
    </td>
  );
};

// ArrayCell Component
const ArrayCell = ({ items, separator }) => {
  return (
    <td className="align-middle">
      <div className="text-sm text-dark">{items.join(separator)}</div>
    </td>
  );
};

// Row Component
const Row = ({ item, columns }) => {
  return (
    <tr className="border-bottom">
      {columns.map((column, index) => {
        if (column.type === "image") {
          return (
            <ImageCell
              key={index}
              alt={`${column.alt} '${item.title}'`}
              src={column.src}
            />
          );
        } else if (column.type === "text") {
          return (
            <TextCell
              key={index}
              text={item[column.key]}
              customRender={column.customRender}
              item={item}
            />
          );
        } else if (column.type === "array") {
          return (
            <ArrayCell
              key={index}
              items={item[column.key]}
              separator={column.separator}
            />
          );
        } else if (column.type === "link") {
          const to = column.to.replace("{title}", encodeURIComponent(item.title));
          return (
            <LinkCell key={index} to={to} text={column.text} />
          );
        } else {
          return null;
        }
      })}
    </tr>
  );
};

export const TmpTable = (img) => {
    return (
        <div className="text-center">
            <img src={img} className="img-fluid" alt="No product" />
        </div>
    );
};


const Table = ({ items, columns, tmp_img }) => {
  return (
    items.length ?
    <div className="table-responsive rounded-4">
      <table className="table border-0 border-bottom border-light">
        <thead>
          <tr>
            {columns.map((column, index) => (
              <td key={index} className="text-secondary bg-light">
                {column.header}
              </td>
            ))}
          </tr>
        </thead>
        <tbody>
          {items.map((item, index) => (
            <Row key={index} item={item} columns={columns} />
          ))}
        </tbody>
      </table>
    </div>
    : TmpTable(tmp_img)
  );
}

export default Table;