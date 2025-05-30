# Stage 1: Build the React Application
ARG NODE_VERSION=24 NGINX_VERSION=1.27.5
FROM node:${NODE_VERSION} AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Setup the Nginx Server to serve the React Application
FROM nginx:${NGINX_VERSION} AS production
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx_default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]