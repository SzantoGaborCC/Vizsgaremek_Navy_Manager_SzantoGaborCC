CREATE TABLE public.user_role
(
    user_id BIGINT NOT NULL,
    "role" VARCHAR(255) NOT NULL,
    UNIQUE (user_id,"role"),
    CONSTRAINT user_user_role_fk FOREIGN KEY (user_id) REFERENCES public.user(id)
);