FROM nginx:alpine
COPY docker/nginx.conf /etc/nginx/nginx.conf
WORKDIR /usr/share/nginx/html
COPY dist .

EXPOSE 80
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]